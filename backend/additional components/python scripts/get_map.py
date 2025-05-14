import numpy as np
from scipy.interpolate import RectBivariateSpline
from scipy.ndimage import gaussian_filter
import rasterio
from PIL import Image
import noise

from CONFIG import *


def generate_perlin_noise(shape, scale=10, octaves=6, persistence=0.5, lacunarity=2.0):
    world = np.zeros(shape)
    for i in range(shape[0]):
        for j in range(shape[1]):
            world[i][j] = noise.pnoise2(i/scale, j/scale,
                                        octaves=octaves,
                                        persistence=persistence,
                                        lacunarity=lacunarity)
    return world


def correlation(bound, x, y):
    return bound.left < x < bound.right and bound.bottom < y < bound.top


def scale_array_nearest(data, scale_factor):
    """Увеличивает массив в `scale_factor` раз, дублируя элементы."""
    return np.repeat(np.repeat(data, scale_factor, axis=0), scale_factor, axis=1)


def get_height(map1: str, map2: str, x: float, y: float, w: int, h: int):
    with rasterio.open(map1) as dataset1:
        with rasterio.open(map2) as dataset2:
            if not (correlation(dataset2.bounds, x, y) or correlation(dataset1.bounds, x, y)):
                return None
            dataset = None
            if correlation(dataset2.bounds, x, y):
                dataset = dataset2
            else:
                dataset = dataset1

            heights = [[0] * w for _ in range(h)]

            for i in range(w):
                for j in range(h):
                    if not correlation(dataset.bounds, x + i, y + j):
                        if correlation(dataset2.bounds, x + i, y + j):
                            dataset = dataset2
                        else:
                            dataset = dataset1
                    row, col = dataset.index(x + i, y + j)
                    heights[j][i] = dataset.read(1, window=((row, row + 1), (col, col + 1)))[0][0]

            return heights


def smooth_bicubic_upscale(original, N, sigma=0.5):
    """
    Детализирует массив (N+2)x(N+2) до (5N)x(5N) с предварительным сглаживанием.

    Параметры:
        original : np.ndarray - исходный массив (N+2)x(N+2).
        N        : int        - размер внутренней области.
        sigma    : float      - параметр размытия Гаусса (0 = нет размытия).
    """
    # Сглаживаем область
    internal_smoothed = gaussian_filter(original, sigma=sigma)

    # Интерполяция с учетом границ (используем весь массив)
    x_old = np.arange(N)
    y_old = np.arange(N)
    spline = RectBivariateSpline(x_old, y_old, internal_smoothed, kx=1, ky=1)  # Линейная по краям

    # Новая сетка (аккуратно near границам)
    x_new = np.linspace(0, N, 5*N)
    y_new = np.linspace(0, N, 5*N)

    detailed = spline(x_new, y_new)
    return detailed


def bicubic_upscale_with_borders(original, N, scale):
    """
    Детализирует массив (N+2)x(N+2) до (5N)x(5N), используя ВСЕ значения,
    включая дополнительные +2 граничных слоя.

    Параметры:
        original : np.ndarray - исходный массив (N+2)x(N+2).
        N        : int        - размер внутренней области (без границ).

    Возвращает:
        np.ndarray - детализированный массив 5N x 5N.
    """
    # Используем ВЕСЬ исходный массив (включая границы)
    x_old = np.arange(N + 2 * scale)  # Координаты исходной сетки [0, 1, ..., N+1]
    y_old = np.arange(N + 2 * scale)
    spline = RectBivariateSpline(x_old, y_old, original)

    # Новая сетка: 5N точек на диапазоне [1, N] (чтобы не выходить за границы)
    x_new = np.linspace(scale, N, 5*N)  # Игнорируем только крайние +0 и N+1
    y_new = np.linspace(scale, N, 5*N)

    # Интерполируем
    detailed = spline(x_new, y_new)
    return detailed


for i in range(len(zones)):
    scale = 2 # 3*5*5 = 750|1500 ; 2*5*2*2*5 = 2000|4000
    N = zones[i][3]
    original = np.array(scale_array_nearest(get_height(SCH_PATH_LEFT, SCH_PATH_TOP, zones[i][0] - 1, zones[i][1] - 1, N + 2, N + 2), scale))
    N *= scale
    data = bicubic_upscale_with_borders(original, N, scale)
    N *= 5

    data = scale_array_nearest(data, 2)
    N *= 2

    perlin_noise = generate_perlin_noise((N, N), scale=20)
    data = data + perlin_noise * 2  # Масштабируем шум

    data = scale_array_nearest(data, 2)
    N *= 2

    data = smooth_bicubic_upscale(data, N)
    N *= 5


    # Находим min и max
    min_val, max_val = np.min(data), np.max(data)

    # Нормализуем в [0, 255]
    normalized_data = ((data - min_val) * (255.0 / (max_val - min_val))).astype(np.uint8)

    # Создаём изображение из массива
    img = Image.fromarray(normalized_data, mode='L')  # 'L' — grayscale (8-bit)

    # Сохраняем в PNG
    img.save('maps/' + zones[i][2] + '.png')
