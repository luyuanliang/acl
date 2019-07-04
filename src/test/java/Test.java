import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.web.acl.domain.SessionAccountDO;

import java.util.Date;

/**
 * Created by luyl on 17-9-18.
 */
public class Test {

    public static void main(String []arg){
        SessionAccountDO sessionAccountDO = new SessionAccountDO();
        sessionAccountDO.setPassword("aaa");
        sessionAccountDO.setPassword("aaa");
        SessionAccountDO newSessionAccountDO = new SessionAccountDO();
        newSessionAccountDO.setAccountName("bb");
        BeanUtils.copyProperties(sessionAccountDO,newSessionAccountDO,"accountName");
        System.out.println(new Gson().toJson(newSessionAccountDO));
        System.out.println(new Date().getTime());
    }
}
