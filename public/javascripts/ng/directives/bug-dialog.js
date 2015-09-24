GameDirectives.directive('bugDialog', [
    'jQuery',

    function($) {
        return {
            restrict: 'E',
            transclude: true,
            scope: {
            	visible:'=',
            	submit:'=',
            	tryNew:'=',
            	isSubmitted:'='
            },
            controller: function($scope) {
            	$scope.$root.$on("open-bug-dialog", function() {
            		$scope.visible = true;
            	});
            	$scope.hide = function() {
            		$scope.visible = false;
            	}
            },
            link: function(scope, element, attrs, ctrl, transclude, formCtrl) {
                

            },
            templateUrl: '/public/templates/bug-form-template.html'
        };
    }
])
