package com.github.rmannibucau.blog.front.security;

import com.github.rmannibucau.blog.front.controller.UserController;
import org.apache.deltaspike.security.api.authorization.AccessDecisionVoter;
import org.apache.deltaspike.security.api.authorization.AccessDecisionVoterContext;
import org.apache.deltaspike.security.api.authorization.SecurityViolation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;

@ApplicationScoped
public class LoggedInUserVoter implements AccessDecisionVoter {
    @Inject
    private UserController user;

    @Override
    public Set<SecurityViolation> checkPermission(final AccessDecisionVoterContext accessDecisionVoterContext) {
        if (!user.isLogged()) {
            final SecurityViolation violation = new SecurityViolation() {
                @Override
                public String getReason() {
                    return "Log in before doing it.";
                }
            };
            return Collections.singleton(violation);
        }
        return Collections.emptySet();
    }
}
