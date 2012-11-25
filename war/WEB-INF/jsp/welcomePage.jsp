<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>V2V</title>
<jsp:include page="commonHeadIncludes.jsp" flush="true" />
</head>
<body>
	<script>
    $(function() {
      $('#commentsBox').dialog(
          {
            autoOpen : false,
            height : 480,
            width : 580,
            modal : true,
            title : "Comments and Feedback on Vein-to-Vein",
            buttons : {
              "Give feedback" : function() {
                console.log($("#commentsContent").val());
                $.ajax({
                  type : "POST",
                  url : "submitFeedback.html",
                  data : {
                    comments : $("#commentsContent").val()
                  },
                  success : function(jsonResponse) {
                    if (jsonResponse["success"] === true) {
                      $.showMessage("Feedback Submitted Successfully!");
                      $("#commentsContent").val("");
                      $('#commentsBox').dialog("close");
                    } else {
                      $.showMessage("Something went wrong."
                          + jsonResponse["errMsg"], {
                        backgroundColor : 'red'
                      });
                    }
                  },
                  error : function() {
                    $.showMessage("Something went wrong."
                        + jsonResponse["errMsg"], {
                      backgroundColor : 'red'
                    });
                    $('#commentsBox').dialog("close");
                  }

                });
              },
              "Cancel" : function() {
                $(this).dialog("close");
              }
            }
          });
      $('#commentsLink').click(function() {
        $('#commentsBox').dialog("open");
      });
    });
  </script>
	<div class="mainBody">
		<div class="mainContent">
			<jsp:include page="topPanel.jsp" flush="true" />
		</div>
		<div class="bottomPanel">
		<span
				class="ui-icon ui-icon-comment" style="display: inline-block;"></span>
			<span class="bottomPanelText" id="commentsLink"> Comments and
				Feedback </span>
				<span
				class="ui-icon ui-icon-mail-closed" style="display: inline-block; margin-left: 20px;"></span><a
				href="mailto:rohit.banga@gatech.edu, vempala@cc.gatech.edu"
				target="_blank">Contact Us</a>
		</div>
		<div id="commentsBox">
			<div>
				<br /> <span style="padding-left: 15px; padding-right: 15px;">
					Your suggestions can help us in making Vein-to-Vein better </span>
				<textarea id="commentsContent" rows="15" cols="50"
					style="margin-top: 20px;"> </textarea>
			</div>
		</div>
	</div>
</body>
</html>
