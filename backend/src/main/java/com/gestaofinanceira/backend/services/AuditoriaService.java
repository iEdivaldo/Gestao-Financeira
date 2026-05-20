package com.gestaofinanceira.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestaofinanceira.backend.model.LogAuditoria;
import com.gestaofinanceira.backend.repository.LogAuditoriaRepository;

@Service
public class AuditoriaService {

    @Autowired
    private LogAuditoriaRepository logAuditoriaRepository;

    public LogAuditoria salvarLog(LogAuditoria logAuditoria) {
        return logAuditoriaRepository.save(logAuditoria);
    }

}
