# 🔐 Spring Boot CSRF & Session Demo

This project demonstrates how sessions and CSRF (Cross-Site Request Forgery) protection work in a Spring Boot application using Spring Security.

---

## 📌 What is a Session and Session ID?

- A **session** is created by the server when a user first makes a request.
- It keeps user-related data (like login status) across multiple requests.
- A **Session ID** is a unique identifier (e.g., `JSESSIONID`) sent to the browser as a cookie and used to maintain this connection.
- You can access it in code via:

```java
request.getSession().getId();
```

---

## 🛡️ What is CSRF and CSRF Token?

**CSRF (Cross-Site Request Forgery)** is an attack where a malicious site tricks your browser into making an unwanted request (like deleting data) while you're logged into another app.

To protect against this, **Spring Security** requires a CSRF token for any state-changing request (`POST`, `PUT`, `DELETE`).

- The token is stored in the session and must be sent with each modifying request.

---

## 🔐 How to Fetch CSRF Token

Spring Security automatically creates a CSRF token when the session is created.

You can expose it using:

```java
@GetMapping("/csrf-token")
public CsrfToken getCsrfToken(HttpServletRequest request) {
    return (CsrfToken) request.getAttribute("_csrf");
}
```

This returns:

```json
{
  "parameterName": "_csrf",
  "headerName": "X-CSRF-TOKEN",
  "token": "abc123..."
}
```

---

## ⚙️ How to Use the CSRF Token in Requests

To perform a `POST` request with CSRF enabled:

1. Make a `GET` request to `/csrf-token`
2. Copy the token from the response
3. Use it in the headers of your `POST`/`PUT`/`DELETE` request:

Example:

```
X-CSRF-TOKEN: <token>
Content-Type: application/json
```

✅ Make sure you also send the `JSESSIONID` cookie with the request (Postman handles this automatically if in the same tab/session).

---

## 🚫 How to Disable CSRF (For APIs)

In your `SecurityConfiguration.java`, add:

```java
http.csrf(csrf -> csrf.disable());
```

This is safe for stateless REST APIs but **should not be done** for browser-based apps with forms.

---

## 🤔 What is `Customizer.withDefaults()`?

Spring Security uses this to apply default settings for common configurations:

```java
http.httpBasic(Customizer.withDefaults());
```

This line enables Basic Authentication using the default configuration.

---

## 🔐 What is the `SecurityConfiguration` File?

This is where you customize how Spring Security secures your app.

Example:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()); // Disables CSRF

        http.authorizeHttpRequests(auth -> auth
            .anyRequest().authenticated() // All requests need login
        );

        http.httpBasic(Customizer.withDefaults()); // Enables basic auth

        http.sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        );

        return http.build();
    }
}
```

---

## 🔁 What is `SessionCreationPolicy`?

This controls when sessions are created:

| Policy         | Meaning                                             |
|----------------|-----------------------------------------------------|
| `ALWAYS`       | Always create a session                             |
| `IF_REQUIRED`  | Create session only if needed (default behavior)    |
| `NEVER`        | Spring won't create session, but will use if exists |
| `STATELESS`    | Never use session at all (best for JWT/token APIs)  |

For REST APIs, prefer:

```java
session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
```

This disables session usage entirely — ideal for scalability and token-based authentication.

---
