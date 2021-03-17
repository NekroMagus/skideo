package net.skideo.service.club;

import lombok.RequiredArgsConstructor;
import net.skideo.client.AuthServiceFeignClient;
import net.skideo.dto.*;
import net.skideo.dto.projections.ClubProfileProjection;
import net.skideo.dto.projections.IdProjection;
import net.skideo.dto.projections.PasswordProjection;
import net.skideo.exception.ClubNotFoundException;
import net.skideo.exception.NotFoundException;
import net.skideo.model.*;
import net.skideo.repository.ClubRepository;
import net.skideo.service.scout.ScoutService;
import net.skideo.service.user.UserService;
import net.skideo.service.video.VideoService;
import org.apache.commons.lang.StringUtils;
import org.aspectj.weaver.patterns.HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.directory.SearchControls;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final UserService userService;
    private final VideoService videoService;
    private final AuthServiceFeignClient feignClient;
    private final PasswordEncoder encoder;

    @Override
    public Club findById(long id) {
        return clubRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Club not found")
        );
    }

    @Override
    public Club findByLogin(String login) {
        return clubRepository.findByInfoLogin(login).orElseThrow(
                () -> new NotFoundException("Club not found")
        );
    }

    @Override
    public void save(Club club) {
        club.getInfo().setPassword(encoder.encode(club.getInfo().getPassword()));
        clubRepository.save(club);
    }

    @Override
    public ClubProfileDto getProfile() {
        final String LOGIN_CURRENT_CLUB = getLoginCurrentClub();
        return clubRepository.findProfileByInfoLogin(LOGIN_CURRENT_CLUB).orElseThrow(
                () -> new NotFoundException("Club not found")
        );
    }


    @Override
    public void addUserToFavorite(long idUser) {
        Club currentClub = getCurrentClub();
        User user = userService.findById(idUser);

        currentClub.getFavoriteUsers().add(user);

        clubRepository.save(currentClub);
    }

    @Override
    public Page<UserShortInfoClubDto> getFavoriteUsers(Pageable pageable) {
        final String LOGIN_CURRENT_CLUB = getLoginCurrentClub();
        return clubRepository.findFavoriteUsersByInfoLogin(LOGIN_CURRENT_CLUB,pageable);
    }

    @Override
    public void updateProfile(ClubProfileDto profile) {
        Club dbClub = getCurrentClub();

        if(StringUtils.isNotBlank(profile.getLogoLink())) {
            dbClub.setLogoLink(profile.getLogoLink());
        }
        if(StringUtils.isNotBlank(profile.getTitleClub())) {
            dbClub.getInfo().setName(profile.getTitleClub());
        }

        clubRepository.save(dbClub);
    }

    @Override
    public void updateLoginAndPassword(String token, AuthDto authDto) {
        feignClient.updateLoginAndPassword(token,authDto);

        Club dbClub = getCurrentClub();

        if(StringUtils.isNotBlank(authDto.getLogin())) {
            dbClub.getInfo().setLogin(authDto.getLogin());
        }
        if(StringUtils.isNotBlank(authDto.getPassword())) {
            dbClub.getInfo().setPassword(authDto.getPassword());
        }

        save(dbClub);
    }

    @Override
    public Club getCurrentClub() {
        return findByLogin(getLoginCurrentClub());
    }

    @Override
    public IdProjection getIdCurrentClub() {
        final String LOGIN_CURRENT_CLUB = getLoginCurrentClub();
        return clubRepository.findClubIdByInfoLogin(LOGIN_CURRENT_CLUB).orElseThrow(
                () -> new NotFoundException("Club not found")
        );
    }

    private String getLoginCurrentClub() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
