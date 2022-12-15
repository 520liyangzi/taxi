package testalipay;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

// /alipay/pay
@RequestMapping("/alipay")
@Controller
@ResponseBody
public class AlipayController {
        //  localhost:9001/alipay/pay?subject=李杨子&pitTradeNo=1002&totalAmount=100
    //ligbqs3062@sandbox.com
    @GetMapping("/pay")
    public String pay(String subject,String pitTradeNo,String totalAmount){
        AlipayTradePagePayResponse response;
        try {
            response = Factory.Payment.Page().pay(subject,pitTradeNo,totalAmount,"");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return response.getBody();
    }

    @PostMapping("/notify")
    public String notify(HttpServletRequest request){
    System.out.println("支付宝回调了");
        String trade_status = request.getParameter("trade_status");
        if (trade_status.trim().equals("TRADE_SUCCESS")){
            Map<String,String> param = new HashMap<>();
            Map<String,String[]> parameterMap = request.getParameterMap();
            for(String name:parameterMap.keySet()){
                param.put(name,request.getParameter(name));
            }

            try {
                if(Factory.Payment.Common().verifyNotify(param)){
            System.out.println("通过支付宝的验证了");
            for (String name : param.keySet()){
            System.out.println("收到的参数：");
            System.out.println(name + "   " + param.get(name));
            }
                }else {
          System.out.println("支付宝验证不通过");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
        return "success";
    }

}
