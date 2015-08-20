GameDirectives.directive('alertDialog', [
		'jQuery',
		function($) {
			return {
				transclude : true,
				restrict : 'E',
				scope : {
					tip: '=',
					imageurl: '@'
				},
				link: function(scope, element, attrs) {
					scope.closeTip = function() {
						tip=null;
					};
					scope.startSideShow = function() {
						scope.$root.$emit("side-show");
					}
				},
				templateUrl : '/public/templates/tip.dialog.html'
			};
		} ]);