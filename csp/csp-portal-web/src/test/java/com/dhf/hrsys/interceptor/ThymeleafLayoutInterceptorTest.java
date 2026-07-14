package com.dhf.hrsys.interceptor;

import com.dhf.energy.EnergyController;
import com.dhf.hrsys.controller.EmployeeController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThymeleafLayoutInterceptorTest {

    private ThymeleafLayoutInterceptor interceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        interceptor = new ThymeleafLayoutInterceptor();
    }

    @Test
    public void testPostHandleWithLayoutAnnotation() throws Exception {
        // 模擬 EnergyController，它帶有 @Layout("frontEnd")
        EnergyController controller = new EnergyController();
        Method method = EnergyController.class.getMethod("index");
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);

        ModelAndView modelAndView = new ModelAndView("energy/index");

        interceptor.postHandle(request, response, handlerMethod, modelAndView);

        assertEquals("layout/frontEnd", modelAndView.getViewName());
        assertEquals("energy/index", modelAndView.getModel().get("view"));
    }

    @Test
    public void testPostHandleWithDefaultLayout() throws Exception {
        // 模擬 EmployeeController，它沒有 @Layout 註解
        EmployeeController controller = new EmployeeController(null, null, null);
        Method method = EmployeeController.class.getMethod("show");
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);

        ModelAndView modelAndView = new ModelAndView("emp/show");

        interceptor.postHandle(request, response, handlerMethod, modelAndView);

        assertEquals("layout/default", modelAndView.getViewName());
        assertEquals("emp/show", modelAndView.getModel().get("view"));
    }

    @Test
    public void testPostHandleWithRedirect() throws Exception {
        ModelAndView modelAndView = new ModelAndView("redirect:/home");

        interceptor.postHandle(request, response, null, modelAndView);

        assertEquals("redirect:/home", modelAndView.getViewName());
    }
}
