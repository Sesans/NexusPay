package com.nexuspay.composition.application;

import com.nexuspay.auth.UserFacade;
import com.nexuspay.composition.application.dto.AccountSummaryDTO;
import com.nexuspay.composition.application.dto.UserSummaryDTO;
import com.nexuspay.composition.domain.exception.UserNotVerifiedException;
import com.nexuspay.ledger.AccountFacade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.nexuspay.auth.domain.model.UserStatus.VERIFIED;

@Service
public class UserService {
    private final UserFacade userFacade;
    private final AccountFacade accountFacade;

    public UserService(UserFacade userFacade, AccountFacade accountFacade) {
        this.userFacade = userFacade;
        this.accountFacade = accountFacade;
    }

    public UserSummaryDTO getUserSummary(UUID userId) {
        var user = userFacade.getUserById(userId);

        if (!user.status().equals(VERIFIED)) {
            throw new UserNotVerifiedException();
        }

        List<AccountSummaryDTO> summaries = accountFacade.getAccountSummariesByUserId(userId)
                .stream()
                .map(accountDto -> new AccountSummaryDTO(
                        accountDto.id(),
                        accountDto.balance(),
                        accountDto.code()
                )).toList();

        return new UserSummaryDTO(
                user.id(),
                user.name(),
                user.status().toString(),
                summaries
        );
    }
}
