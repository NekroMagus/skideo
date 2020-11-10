package net.skideo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skideo.dto.projections.ClubProfileProjection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubProfileDto {

    private String titleClub;
    private String logoLink;

    public ClubProfileDto(ClubProfileProjection club) {
        this.titleClub=club.getTitleClub();
        this.logoLink=club.getLogoLink();
    }

}
