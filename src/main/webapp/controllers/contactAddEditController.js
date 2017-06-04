var contactAddEditController;

contactAddEditController = function($scope, $http, $state, Service) {
    $scope.contact = Service.contact;
	$scope.submitted = false;
	
	$scope.save = function() {

		$scope.submitted = true;

		if ($scope.contact.name != null && $scope.contact.name != "") {
			$http.post('/contacts', $scope.contact)
			.then(function(res){$state.go('main.contacts')},
			      function(err){return "err";});
		}

	};

	$scope.addMorePhones = function() {
		$scope.contact.phones.push('');
	}; 

	$scope.addMoreEmails = function() {
		$scope.contact.emails.push('');
	};

	$scope.deletePhone = function(index){
		if (index > -1) {
    		$scope.contact.phones.splice(index, 1);
		}

		if ($scope.contact.phones.length < 1){
			$scope.addMorePhones();
		}
	};

	$scope.deleteEmail = function(index){
		if (index > -1) {
    		$scope.contact.emails.splice(index, 1);
		}

		if ($scope.contact.emails.length < 1){
			$scope.addMoreEmails();
		}
	};

};

angular.module('avaliacandidatos').controller("contactAddEditController", contactAddEditController);