package docker.dockerinterlockconnection.util;

import docker.dockerinterlockconnection.dto.response.CommandExecuteResponse;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CommandUtil {
    @SneakyThrows
    public static CommandExecuteResponse readProcessPrintData(Process process) {
        InputStream resultInputStream = process.getInputStream();
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        CommandExecuteResponse response = new CommandExecuteResponse();
        byte[] buffer = new byte[1024];
        int length = 0;

        while ((length = resultInputStream.read(buffer)) != -1) {
            resultStream.write(buffer);
        }

        //        process.waitFor();
        process.waitFor();
        ByteArrayOutputStream errorOutputStream = new ByteArrayOutputStream();
        String resultString = resultStream.toString(String.valueOf(StandardCharsets.UTF_8));
        resultStream.close();
        resultInputStream.close();
        response.setSuccess(true);

        int exitValue = process.exitValue();

        if (exitValue != 0) {
            InputStream errorInputStream = process.getErrorStream();
            while ((length = errorInputStream.read(buffer)) != -1) {
                errorOutputStream.write(buffer);
            }
            String errorString = errorOutputStream.toString(String.valueOf(StandardCharsets.UTF_8));
            response.setData(errorString);
            errorOutputStream.close();
            errorInputStream.close();
            response.setSuccess(false);
            response.setErrorCode(String.valueOf(exitValue));
            return response;
        }

        process.destroy();
//        response.setData(resultString.isBlank() ? null : resultString.strip().replace("\u0000", " "));
        String result = resultString.isEmpty() ? null : resultString.trim();
        if (result != null){
            response.setData(result);
        }
        return response;
    }
}
