package com.github.nenidan.ne_ne_challenge.domain.challenge.presentation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.github.nenidan.ne_ne_challenge.domain.challenge.application.command.dto.CreateChallengeCommand;
import com.github.nenidan.ne_ne_challenge.domain.challenge.application.command.dto.CreateHistoryCommand;
import com.github.nenidan.ne_ne_challenge.domain.challenge.application.command.dto.UpdateChallengeInfoCommand;
import com.github.nenidan.ne_ne_challenge.domain.challenge.application.query.dto.request.ChallengeSearchCond;
import com.github.nenidan.ne_ne_challenge.domain.challenge.application.query.dto.request.HistorySearchCond;
import com.github.nenidan.ne_ne_challenge.domain.challenge.presentation.dto.request.ChallengeSearchRequest;
import com.github.nenidan.ne_ne_challenge.domain.challenge.presentation.dto.request.CreateChallengeRequest;
import com.github.nenidan.ne_ne_challenge.domain.challenge.presentation.dto.request.CreateHistoryRequest;
import com.github.nenidan.ne_ne_challenge.domain.challenge.presentation.dto.request.HistorySearchRequest;
import com.github.nenidan.ne_ne_challenge.domain.challenge.presentation.dto.request.UpdateChallengeInfoRequest;

@Mapper
public interface ChallengeRequestMapper {
    ChallengeRequestMapper INSTANCE = Mappers.getMapper(ChallengeRequestMapper.class);

    CreateChallengeCommand toCommand(CreateChallengeRequest request);
    UpdateChallengeInfoCommand toCommand(UpdateChallengeInfoRequest request);
    CreateHistoryCommand toCommand(CreateHistoryRequest request);
    ChallengeSearchCond toCond(ChallengeSearchRequest request);
    HistorySearchCond toCond(HistorySearchRequest request);
}
