package ru.rksp.klosep.gate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.rksp.klosep.gate.api.StudentGateApi;
import ru.rksp.klosep.gate.client.api.StudentDataApi;
import ru.rksp.klosep.gate.client.model.StudentDataCreateRequest;
import ru.rksp.klosep.gate.client.model.StudentDataResponse;
import ru.rksp.klosep.gate.model.StudentGateCreateRequest;
import ru.rksp.klosep.gate.model.StudentGateResponse;

@RestController
@RequiredArgsConstructor
public class StudentGateController implements StudentGateApi {

    private final StudentDataApi studentsFeignClient;

    /**
     * Проксирует создание студента во внутренний DATA-SERVICE.
     * Получает запрос от клиента, преобразует модель и перенаправляет.
     */
    @Override
    public ResponseEntity<StudentGateResponse> createStudent(StudentGateCreateRequest request) {
        StudentDataCreateRequest dataRequest = new StudentDataCreateRequest();
        dataRequest.setFullName(request.getFullName());
        dataRequest.setPassport(request.getPassport());

        StudentDataResponse dataResponse = studentsFeignClient.createStudentDataInData(dataRequest);

        StudentGateResponse gateResponse = new StudentGateResponse();
        gateResponse.setId(dataResponse.getId());
        gateResponse.setFullName(dataResponse.getFullName());
        gateResponse.setPassport(dataResponse.getPassport());

        return ResponseEntity.status(201).body(gateResponse);
    }
}

