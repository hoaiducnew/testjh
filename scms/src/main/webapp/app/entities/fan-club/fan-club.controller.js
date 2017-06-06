(function() {
    'use strict';

    angular
        .module('scmsApp')
        .controller('FanClubController', FanClubController);

    FanClubController.$inject = ['FanClub'];

    function FanClubController(FanClub) {

        var vm = this;

        vm.fanClubs = [];

        loadAll();

        function loadAll() {
            FanClub.query(function(result) {
                vm.fanClubs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
