
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>RFC Searcher</title>
</head>
<body>
    <div>
        <form style="text-align: center" action="<%= request.getContextPath() %>/search" method="post">
            <input type="hidden" name="action" value="search" >
            <input name="search" placeholder="Поиск" maxlength="50" type="text"  required>
            <input type="submit" value="Найти" >
        </form>
    </div>
</body>
</html>
