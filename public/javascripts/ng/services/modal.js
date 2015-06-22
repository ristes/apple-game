Game.factory('Modal', ['$modal', function ($modal) {
    return {
        start: function ( tUrl, sc) {
            return $modal.open({
                backdrop: 'static',
                templateUrl: tUrl,
                windowClass: 'app-modal-window',
                size: 'lg',
                scope: sc
            });
        }
    }

}])