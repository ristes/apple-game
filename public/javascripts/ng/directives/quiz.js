GameDirectives.directive('quizWindow', ['jQuery', '$window','State','Quiz',
    function($, $window,State,Quiz) {

      return {
        restrict: 'E',
        transclude: true,
      
        link: function($scope, element, attrs, ctrl, transclude, formCtrl) {

        	$scope.quizvisible = false;
        	$scope.questions = [];
        	var onDataReceived = function(data) {
        		$scope.questions = data;
        	}
        	State.subscribe('quiz','QuizController',onDataReceived);
        	$scope.answer = function(answer) {
        		ordnum++;
        		$scope.question = $scope.questions[ordnum];
        	}
        	
        	
        	$scope.question = {};
        	$scope.ordnum=0;
        	$scope.data = [];
        	
        	$scope.next = function() {
        		$scope.question = data[ordnum];
        	}
        	$scope.show = function() {
                $scope.$root.$emit("shop-hide");
                $scope.bgw = $window.innerWidth;
                $scope.bgh = $window.innerHeight;
                $scope.quizvisible = true;
             }
        	 $scope.hide = function() {
                 $scope.bgw = 0;
                 $scope.bgh = 0;
                 $scope.quizvisible = false;
               }
        	 $scope.$root.$on("quiz-start", function() {
        		 $scope.show();
        	 });
        	 var un = $scope.$root.$on("quiz-end", function() {
                 $scope.hide();
               });
        },
        templateUrl: '/public/templates/quiz-window.html'
      };

    }]);