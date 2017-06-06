(function() {
    'use strict';

    angular
        .module('scmsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('fan-club', {
            parent: 'entity',
            url: '/fan-club',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FanClubs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fan-club/fan-clubs.html',
                    controller: 'FanClubController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('fan-club-detail', {
            parent: 'fan-club',
            url: '/fan-club/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FanClub'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fan-club/fan-club-detail.html',
                    controller: 'FanClubDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FanClub', function($stateParams, FanClub) {
                    return FanClub.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'fan-club',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('fan-club-detail.edit', {
            parent: 'fan-club-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fan-club/fan-club-dialog.html',
                    controller: 'FanClubDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FanClub', function(FanClub) {
                            return FanClub.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fan-club.new', {
            parent: 'fan-club',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fan-club/fan-club-dialog.html',
                    controller: 'FanClubDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                leader: null,
                                address: null,
                                email: null,
                                phone: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('fan-club', null, { reload: 'fan-club' });
                }, function() {
                    $state.go('fan-club');
                });
            }]
        })
        .state('fan-club.edit', {
            parent: 'fan-club',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fan-club/fan-club-dialog.html',
                    controller: 'FanClubDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FanClub', function(FanClub) {
                            return FanClub.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fan-club', null, { reload: 'fan-club' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fan-club.delete', {
            parent: 'fan-club',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fan-club/fan-club-delete-dialog.html',
                    controller: 'FanClubDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FanClub', function(FanClub) {
                            return FanClub.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fan-club', null, { reload: 'fan-club' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
