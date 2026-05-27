package com.example.departmentservice.service;

import com.example.departmentservice.entity.Department;
import com.example.departmentservice.repo.DepartmentRepo;
import com.example.shared.grpc.DepartmentGrpcServiceGrpc;
import com.example.shared.grpc.DepartmentRequest;
import com.example.shared.grpc.DepartmentResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class DepartmentGrpcServiceImpl extends DepartmentGrpcServiceGrpc.DepartmentGrpcServiceImplBase {

    private final DepartmentRepo departmentRepo;

    @Override
    public void getDepartment(DepartmentRequest request, StreamObserver<DepartmentResponse> responseObserver) {
        try {
            UUID departmentId = UUID.fromString(request.getDepartmentId());
            Department department = departmentRepo.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            DepartmentResponse response = DepartmentResponse.newBuilder()
                    .setId(department.getId().toString())
                    .setName(department.getName())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}
