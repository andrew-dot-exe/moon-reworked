import numpy as np
from scipy.interpolate import RectBivariateSpline
import rasterio
from PIL import Image

from CONFIG import *


def correlation(bound, x, y):
    return bound.left < x < bound.right and bound.bottom < y < bound.top


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


def bicubic_upscale_with_borders(original, N):
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
    x_old = np.arange(N + 2)  # Координаты исходной сетки [0, 1, ..., N+1]
    y_old = np.arange(N + 2)
    spline = RectBivariateSpline(x_old, y_old, original)

    # Новая сетка: 5N точек на диапазоне [1, N] (чтобы не выходить за границы)
    x_new = np.linspace(1, N, 5*N)  # Игнорируем только крайние +0 и N+1
    y_new = np.linspace(1, N, 5*N)
    
    # Интерполируем
    detailed = spline(x_new, y_new)
    return detailed


for i in range(len(zones)):
    N = zones[i][3]
    original = get_height(SCH_PATH_LEFT, SCH_PATH_TOP, zones[i][0] - 1, zones[i][1] - 1, N + 2, N + 2)
    data = np.array(bicubic_upscale_with_borders(original, N))
    # Находим min и max
    min_val, max_val = np.min(data), np.max(data)

    # Нормализуем в [0, 255]
    normalized_data = ((data - min_val) * (255.0 / (max_val - min_val))).astype(np.uint8)

    # Создаём изображение из массива
    img = Image.fromarray(normalized_data, mode='L')  # 'L' — grayscale (8-bit)

    # Сохраняем в PNG
    img.save('maps/' + zones[i][2] + '.png')

    data = original
    # Находим min и max
    min_val, max_val = np.min(data), np.max(data)

    # Нормализуем в [0, 255]
    normalized_data = ((data - min_val) * (255.0 / (max_val - min_val))).astype(np.uint8)

    # Создаём изображение из массива
    img = Image.fromarray(normalized_data, mode='L')  # 'L' — grayscale (8-bit)

    # Сохраняем в PNG
    img.save('maps/' + zones[i][2] + 'befor.png')

