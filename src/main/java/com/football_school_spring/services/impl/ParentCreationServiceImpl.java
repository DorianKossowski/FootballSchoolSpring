package com.football_school_spring.services.impl;

import com.football_school_spring.models.Parent;
import com.football_school_spring.models.enums.UserStatusName;
import com.football_school_spring.models.enums.UserTypeName;
import com.football_school_spring.repositories.ParentRepository;
import com.football_school_spring.repositories.UserStatusRepository;
import com.football_school_spring.repositories.UserTypeRepository;
import com.football_school_spring.services.ParentCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParentCreationServiceImpl implements ParentCreationService {
    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private UserTypeRepository userTypeRepository;
    @Autowired
    private ParentRepository parentRepository;

    @Override
    public void createParent(Parent parent) {
        parent.setUserStatus(userStatusRepository.getByName(UserStatusName.WAITING_FOR_APPROVAL.getName()));
        parent.setUserType(userTypeRepository.getByName(UserTypeName.PARENT.getName()));
        parentRepository.save(parent);
    }
}