var contactListController;

contactListController = function($scope, $http, $state, Service) {
	$scope.contacts = [];
	$scope.preDeletedContact = {};
	Service.clean();

	$scope.init = function() {
		$scope.listAllContacts();
	};
	
	$scope.listAllContacts = function() {
	    $http.get("/contacts").then(function(res){$scope.contacts=res.data;}, function(err){})
	};

	$scope.preDelete = function(contact) {
		$scope.preDeletedContact = contact;
		$('#myModal').modal('show');
	};

	$scope.delete = function() {
		if($scope.preDeletedContact != null) {
		    $('#myModal').modal('hide');
            $http.delete("/contacts/" + $scope.preDeletedContact.id)
            .then(function(res){$state.reload();}, function(err){alert(err);});
		}
	};

	$scope.edit = function(contact) {
	    Service.contact = contact;
	    $state.go("main.addeditcontact");
	}

	$scope.bday = function(c) {
		if(c.birthDay==null || c.birthDay == ""){
			return "";
		} else {
			return c.birthDay + "/" + c.birthMonth + "/" + c.birthYear;
		}
	};
};

angular.module('avaliacandidatos').controller("contactListController", contactListController);