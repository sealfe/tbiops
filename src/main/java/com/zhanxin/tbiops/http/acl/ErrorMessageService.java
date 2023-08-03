package com.zhanxin.tbiops.http.acl;

import com.zhanxin.tbiops.repository.ErrorMessageConvert;
import com.zhanxin.tbiops.repository.ErrorMessageConvertRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ErrorMessageService {


    private ErrorMessageConvertRepository errorMessageConvertRepository;


    public Map<String, String> getErrorMsgMap() {
        List<ErrorMessageConvert> all = errorMessageConvertRepository.findAll();
        return all.stream().collect(Collectors.toMap(ErrorMessageConvert::key, ErrorMessageConvert::getCnMessage));
    }

    public String getCnMessage(String bkErrorMsg, String requestURL) {
        Map<String, String> errorMsgMap = getErrorMsgMap();
        for (Map.Entry<String, String> stringStringEntry : errorMsgMap.entrySet()) {
            String key = stringStringEntry.getKey();
            String value = stringStringEntry.getValue();
            if (bkErrorMsg.contains(key) && requestURL.contains(key)) {
                return value;
            }
        }
        return bkErrorMsg;
    }
}
