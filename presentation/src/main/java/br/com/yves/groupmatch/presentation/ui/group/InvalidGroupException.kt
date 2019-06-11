package br.com.yves.groupmatch.presentation.ui.group

class InvalidGroupException(groupId: String): Exception("Group with id: $groupId does not exist")