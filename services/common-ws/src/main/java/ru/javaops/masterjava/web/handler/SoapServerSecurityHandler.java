package ru.javaops.masterjava.web.handler;

import com.sun.xml.ws.api.handler.MessageHandlerContext;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.web.AuthUtil;
import ru.javaops.masterjava.web.Statistics;

import javax.xml.ws.handler.MessageContext;
import java.util.List;
import java.util.Map;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

/**
 * @author Pavel Zaytsev
 * <p>
 * Created on 09.03.2019
 */
@Slf4j
//public class SoapServerSecurityHandler implements SOAPHandler<SOAPMessageContext> { //work
public class SoapServerSecurityHandler extends SoapBaseHandler {

    @Override
    public boolean handleMessage(MessageHandlerContext context) {
        checkAuth(context, Statistics.RESULT.SUCCESS);
        return true;
    }

    @Override
    public boolean handleFault(MessageHandlerContext context) {
        checkAuth(context, Statistics.RESULT.FAIL);
        return true;
    }

    private void checkAuth(MessageHandlerContext context, Statistics.RESULT result) {
        Map<String, List<String>> headers = (Map<String, List<String>>) context.get(MessageContext.HTTP_REQUEST_HEADERS);

        int code = AuthUtil.checkBasicAuth(headers, AuthUtil.encodeBasicAuthHeader("user", "password"));
        if (code != 0) {
            context.put(MessageContext.HTTP_RESPONSE_CODE, code);
            throw new SecurityException();
        }
        log.info("Auth with " + headers.get(AUTHORIZATION) + " is " + result.name());
    }
}
