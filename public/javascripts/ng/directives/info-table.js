GameDirectives.directive('infoTable', ['jQuery','$items', function($,$items) {

  return {
    restrict: 'E',
    transclude: true,
    scope: {},
    link: function(scope, element, attrs, ctrl, transclude, formCtrl) {
      scope.showNext = true;	
      scope.visible = false;

     
      
     

      var unregShow = scope.$root.$on("info-show", function($scope, cfg) {
        scope.$root.$emit('side-hide');
        scope.infos = cfg.infos;
        scope.title = "Info";
        scope.titleImageUrl = cfg.titleImageUrl;
        if (cfg.showNext !== null) {
          scope.showNext = cfg.showNext;
          if (cfg.onHide) {
            scope.hideFn = cfg.onHide;
          }
        }
       
        scope.visible = true;
      });

     

      var unregHide = scope.$root.$on("info-hide", function() {
        scope.hide();
      });

      scope.hide = function() {
        scope.visible = false;
        scope.buying = false;
        if (scope.hideFn) {
          scope.hideFn();
        }
      }

      scope.$on("$destroy", function() {
        unregHide();
        unregShow();
      });

    },
    templateUrl: '/public/templates/info-table.html'
  };

}])