package ba.unsa.etf.nwt.system_events_service.service;

import ba.unsa.etf.nwt.system_events_service.actions.grpc.ActionsRequest;
import ba.unsa.etf.nwt.system_events_service.actions.grpc.ActionsResponse;
import ba.unsa.etf.nwt.system_events_service.actions.grpc.ActionsServiceGrpc;
import ba.unsa.etf.nwt.system_events_service.model.Action;
import ba.unsa.etf.nwt.system_events_service.repository.ActionRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class ActionService extends ActionsServiceGrpc.ActionsServiceImplBase {

    @Autowired
    private ActionRepository actionRepository;

    @Override
    public void save(ActionsRequest request, StreamObserver<ActionsResponse> responseObserver){
        Action action = new Action();
        action.setTimestamp(request.getTimestamp());
        action.setMicroservice(request.getMicroservice());
        action.setActionType(request.getActionType());
        action.setResourceName(request.getResourceName());
        action.setResponseType(request.getResponseType());
        action.setUsername(request.getUsername());
        actionRepository.save(action);

        ActionsResponse response = ActionsResponse.newBuilder()
                .setStatus("OK")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
