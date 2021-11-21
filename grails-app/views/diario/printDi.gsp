<%--
  Created by IntelliJ IDEA.
  User: Fanisso
  Date: 9/29/2020
  Time: 8:49 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <script type="text/javascript">
        function loadDiario() {
            window.open('/MALEYANGA/diario/printDiario', '_blank');

        }
    </script>
</head>

<body>
<button onclick="loadDiario()"><glyph:icon iconName="printer"/>Diário Geral</button>

</body>
</html>