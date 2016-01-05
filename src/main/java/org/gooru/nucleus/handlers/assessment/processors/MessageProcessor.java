package org.gooru.nucleus.handlers.assessment.processors;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.gooru.nucleus.handlers.assessment.constants.MessageConstants;
import org.gooru.nucleus.handlers.assessment.processors.exceptions.InvalidRequestException;
import org.gooru.nucleus.handlers.assessment.processors.exceptions.InvalidUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MessageProcessor implements Processor {

  private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);
  private Message<Object> message;
  String userId;
  JsonObject prefs;
  JsonObject request;
  
  public MessageProcessor(Message<Object> message) {
    this.message = message;
  }
  
  @Override
  public JsonObject process() {
    JsonObject result;
    try {
      if (message == null || !(message.body() instanceof JsonObject)) {
        LOGGER.error("Invalid message received, either null or body of message is not JsonObject ");
        throw new InvalidRequestException();
      }
      
      final String msgOp = message.headers().get(MessageConstants.MSG_HEADER_OP);
      userId = ((JsonObject)message.body()).getString(MessageConstants.MSG_USER_ID);
      if (userId == null) {
        LOGGER.error("Invalid user id passed. Not authorized.");
        throw new InvalidUserException();
      }
      prefs = ((JsonObject)message.body()).getJsonObject(MessageConstants.MSG_KEY_PREFS);
      request = ((JsonObject)message.body()).getJsonObject(MessageConstants.MSG_HTTP_BODY);
      switch (msgOp) {
      case MessageConstants.MSG_OP_ASSESS_CREATE:
        result = processAssessmentCreate();
        break;
      case MessageConstants.MSG_OP_ASSESS_GET:
        result = processAssessmentGet();
        break;
      case MessageConstants.MSG_OP_ASSESS_UPDATE:
        result = processAssessmentUpdate();
        break;
      default:
        LOGGER.error("Invalid operation type passed in, not able to handle");
        throw new InvalidRequestException();
      }
      return result;
    } catch (InvalidRequestException e) {
      // TODO: handle exception
    } catch (InvalidUserException e) {
      // TODO: handle exception
    }

    return null;
  }

  private JsonObject processAssessmentUpdate() {
    // TODO Auto-generated method stub
    String assessmentId = message.headers().get(MessageConstants.ASSESSMENT_ID);
    
    return null;    
  }

  private JsonObject processAssessmentGet() {
    // TODO Auto-generated method stub
    String assessmentId = message.headers().get(MessageConstants.ASSESSMENT_ID);
    
    return null;
  }

  private JsonObject processAssessmentCreate() {
    // TODO Auto-generated method stub
    
    return null;    
  }

}