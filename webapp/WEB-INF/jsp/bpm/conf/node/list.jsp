<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader/>
<div data-table="table" class="panel">

    <es:showMessage/>

    <div class="row-fluid tool ui-toolbar">
        <div class="span4">
                
	        <a href="<es:BackURL/>">
	            <i class="icon-reply"></i>
	            返回
	        </a>
        </div>
    </div>
    
    
    <h4 class="hr">流程</h4>
    <div>
		<div class="control-group span4">
			流程名称: ${bpmProcess.name} 
		</div>

		<div class="control-group span4">
			描述：${bpmProcess.descn} 
		</div>
		<div class="clearfix"></div>
	</div>
    
    <%@include file="listTable.jsp"%>

</div>

<es:contentFooter/>

</script>