<#assign content>

<h2 class="boardList">Saved Boggle Boards</h2>

<#if boards?size == 0>
<p>
There aren't any saved boards.
<a href="/play">Play a new board.</a>
</p>
<#else>
<table align="center" class="boardList">
	<tr>
		<th>Board ID</th>
		<th># of Guesses</th>
	</tr>
	<#list boards?keys as id>
	<tr>
		<td><a href="/board?id=${id}">${id}</a></td>
		<td>${boards[id]}</td>
	</tr>
	</#list>
</#if>

</#assign>
<#include "main.ftl">
