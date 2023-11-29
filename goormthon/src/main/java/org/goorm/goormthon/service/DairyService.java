package org.goorm.goormthon.service;

import lombok.RequiredArgsConstructor;
import org.goorm.goormthon.repository.DairyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DairyService {

    private final DairyRepository dairyRepository;




}
