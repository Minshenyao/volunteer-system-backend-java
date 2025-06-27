package edu.ahut.volunteersystembackend.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ahut.volunteersystembackend.dao.UserRepository;
import edu.ahut.volunteersystembackend.model.User;
import edu.ahut.volunteersystembackend.response.Response;
import edu.ahut.volunteersystembackend.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class AdminMiddleware implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AdminMiddleware.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;

    public AdminMiddleware(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return handleAuthError(response, 401, "非法的请求");
        }

        // 提取JWT令牌
        String tokenStr = authHeader.substring(7);

        // 验证令牌
        if (!JwtUtils.validateToken(tokenStr)) {
            return handleAuthError(response, 401, "无效的令牌");
        }

        // 检查令牌是否过期
        if (JwtUtils.isTokenExpired(tokenStr)) {
            return handleAuthError(response, 401, "令牌已过期");
        }

        try {
            // 从令牌中获取用户信息
            Number userIdNumber = (Number) JwtUtils.getClaim(tokenStr, "id");
            Long userId = userIdNumber.longValue();

            String email = JwtUtils.getClaim(tokenStr, "email") != null ?
                    JwtUtils.getClaim(tokenStr, "email").toString() : null;

            if (email == null) {
                return handleAuthError(response, 401, "无效的用户信息");
            }

            // 查询用户信息，验证管理员权限
            User user = userRepository.findByEmail(email);
            if (user == null) {
                return handleAuthError(response, 401, "用户不存在");
            }

            // 检查用户是否是管理员
            if (user.getAdmin() == null || !user.getAdmin()) {
                return handleAuthError(response, 403, "需要管理员权限");
            }

            // 将用户信息保存到请求属性中，以便在控制器中使用
            request.setAttribute("userId", userId);
            request.setAttribute("email", email);
            request.setAttribute("nickname", user.getNickname());
            request.setAttribute("isAdmin", true);

            return true;
        } catch (Exception e) {
            log.error("处理管理员认证时发生错误", e);
            return handleAuthError(response, 500, "处理管理员认证时发生错误: " + e.getMessage());
        }
    }

    private boolean handleAuthError(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(Response.error(statusCode, message)));
        return false;
    }
}