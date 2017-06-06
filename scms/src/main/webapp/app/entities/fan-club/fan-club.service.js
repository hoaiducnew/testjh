(function() {
    'use strict';
    angular
        .module('scmsApp')
        .factory('FanClub', FanClub);

    FanClub.$inject = ['$resource'];

    function FanClub ($resource) {
        var resourceUrl =  'api/fan-clubs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
