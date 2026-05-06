package org.example.affaci.Models.DTO;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.affaci.Models.Enum.Unit;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DetailProductResponseDTO {

    UUID id;
    String name;
    String regionName;
    String categoryName;
    List<ChemicalDTO> chemicalComposition;
    List<MineralDTO> mineralComposition;
    List<VitaminDTO> vitaminComposition;
    List<FattyAcidDTO> fattyAcidComposition;
    List<AminoAcidDTO> aminoAcidComposition;
    List<AminoAcidRatioDTO> aminoAcidRatios;


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ChemicalDTO{
        UUID id;
        private String compoundName;
        private String compoundCategory;
        private Double quantity;
        private Double error;
        Unit unit;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class MineralDTO{
        private UUID id;
        private String mineral;
        private Double quantity;
        private Double error;
        Unit unit;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class VitaminDTO{
        private UUID id;
        private String vitaminName;
        private String vitaminGroup;
        private Double quantity;
        private Double error;
        private Unit unit;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class FattyAcidDTO{
        private UUID id;
        private String fattyAcidName;
        private String typeOfFattyAcid;
        private Double quantity;
        private Double error;
        private Unit unit;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AminoAcidDTO{
        private UUID id;
        private String aminoAcidName;
        private Double quantity;
        private Double error;
        private Unit unit;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AminoAcidRatioDTO{
        private UUID id;
        private String ratioName;
        private Double quantity;
        private Double error;
        private Unit unit;
    }


}
