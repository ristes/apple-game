GameDirectives.directive('sideMenu', ['jQuery', '$window','BoughtItems',
    function($, $window,BoughtItems) {

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

    }]).directive('resumeDialog', ['jQuery', '$window','$modal','$day','State','Resume',
                               function($, $window, $modal, day, State, Resume) {

        return {
          restrict: 'E',
          transclude: true,
          scope: {
          },
          link: function(scope, element, attrs, ctrl, transclude, formCtrl) {
           
           
           var windowTemplateUrl = '/public/templates/resume-dialog.html';
            scope.visible = false;
           scope.$root.$on("resume-open", function() {
        	   Resume.load();
        	   State.subscribe('resume','resumeDirective',function(data) {
        		   scope.resumeData = data;
        		   scope.$apply();
        	   });
        	   scope.visible = true;
//        	   scope.bgw = $window.innerWidth;
//               scope.bgh = $window.innerHeight;
//        	   scope.notification = $modal.open({
//                   backdrop: 'static',
//                   templateUrl: windowTemplateUrl,
//                   scope: scope
//                 });
        	   scope.$root.$on("resume-close", function() {
        		   scope.bgw = 0;
                   scope.bgh = 0;
//        		   scope.notification.dismiss('cancel');
        		   Resume.setSeen();
               });
           });
           
           scope.close = function() {
        	   scope.visible=false;
        	   scope.$root.$emit("resume-close");
           }
           
           scope.$root.$watch("day.isNewSeason", function(n) {
        	   if (!n) return;
        	   if (n===true) {
        		   scope.$root.$emit("resume-open");
        	   }
        	   
           })
           
         
          },
          templateUrl: '/public/templates/resume-dialog.html'
        };

      }]);