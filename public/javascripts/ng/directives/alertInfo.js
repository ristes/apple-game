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
				},
				templateUrl : '/public/templates/tip.dialog.html'
			};
		} ]);