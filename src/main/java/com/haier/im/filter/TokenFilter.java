package com.haier.im.filter;

import com.alibaba.fastjson.JSON;
import com.haier.im.base.IMRespEnum;
import com.haier.im.base.RespResult;
import com.haier.im.base.SpringUtils;
import com.haier.im.service.IMAccountTokenService;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

@Component
@WebFilter(urlPatterns = "/im/*",filterName = "tokenFilter")
@Order(1)
public class TokenFilter implements Filter {

    @Resource
    private IMAccountTokenService imAccountTokenService;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String uri = req.getRequestURI();
        RespResult respResult = new RespResult();
        PrintWriter writer = null;
        OutputStreamWriter osw = null;
        //对请求的uri(即api)进行判断，如果是登录的uri则直接放行，如果是其他api则对sign进行验证操作
        if( uri.contains("sec/") ){
            String token = req.getHeader("token");
            if (token != null && StringUtils.isNotEmpty(token.trim())){
                if(imAccountTokenService == null){
                    imAccountTokenService = (IMAccountTokenService) SpringUtils.getBean("imAccountTokenService");
                }
                boolean tokenFlag = imAccountTokenService.checkToken(token);
                if (tokenFlag){
                    //通过
                    filterChain.doFilter(servletRequest,servletResponse);
                    return;
                }else{
                    respResult.setCode(IMRespEnum.AUTH_FAILD.getCode());
                    respResult.setMsg(IMRespEnum.AUTH_FAILD.getMsg());
                    osw = new OutputStreamWriter(servletResponse.getOutputStream(),
                            "UTF-8");
                    writer = new PrintWriter(osw, true);
                    String jsonStr = JSON.toJSONString(respResult);
                    writer.write(jsonStr);
                    writer.flush();
                    writer.close();
                    osw.close();
                }
            }else{
                respResult.setCode(IMRespEnum.AUTH_FAILD.getCode());
                respResult.setMsg(IMRespEnum.AUTH_FAILD.getMsg());
                osw = new OutputStreamWriter(servletResponse.getOutputStream(),
                        "UTF-8");
                writer = new PrintWriter(osw, true);
                String jsonStr = JSON.toJSONString(respResult);
                writer.write(jsonStr);
                writer.flush();
                writer.close();
                osw.close();
            }
        }else{
            //登录操作时候，放行到登录接口
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
    }

    @Override
    public void destroy() {

    }
}
