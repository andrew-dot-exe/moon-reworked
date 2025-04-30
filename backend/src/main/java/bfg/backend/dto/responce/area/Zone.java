package bfg.backend.dto.responce.area;

public record Zone(String name,
                   Integer widthSecond,
                   Integer longitudeSecond,
                   Integer illumination,
                   int[] ways) {
}
