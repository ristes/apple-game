GameDirectives.directive('sideMenu', ['jQuery', '$window',
    function($, $window) {

      return {
        restrict: 'E',
        transclude: true,
        scope: {
          weather: '=',
          actions: '='
        },
        link: function(scope, element, attrs, ctrl, transclude, formCtrl) {
          scope.itemClick = function(a) {
            scope.$root.$emit('operation-' + a.name, a);
          }
          scope.bgw = 0;
          scope.bgh = 0;

          scope.visible = false;
          
          scope.executeItem = function(item) {
        	  console.log(item);
          }

          scope.show = function() {
            scope.$root.$emit("shop-hide");
            scope.bgw = $window.innerWidth;
            scope.bgh = $window.innerHeight;
            scope.visible = true;
          }
          scope.hide = function() {
            scope.bgw = 0;
            scope.bgh = 0;
            scope.visible = false;
          }

          var un = scope.$root.$on("side-hide", function() {
            scope.hide();
          });

          scope.$on('$destroy', function() {
            if (un) {
              un();
            }
          })

          scope.onClick = function() {
            if (scope.visible) {
              scope.hide();
            } else {
              scope.show();
            }
          }
        },
        templateUrl: '/public/templates/side-menu.html'
      };

    }]);