'use strict';

// Update cache names any time any of the cached files change.
const CACHE_NAME = 'static-cache-v3';

// Add list of files to cache here.
const FILES_TO_CACHE = [
    '/index.html',
    '/global.css',
    '/build/bundle.css',
    '/build/bundle.js',
    '/cinema.jpeg',
    '/cinema-maskable-icon.png',
    '/movie-transparent-icon.png',
    '/favicon.png',
    'netflix-icon.png',
    '/disney-plus-icon.png',
    '/globoplay-icon.png',
    '/hbo-icon.png',
    '/prime-icon.png',
    '/telecine-icon.png',
];

self.addEventListener('install', (evt) => {
    console.log('[ServiceWorker] Install');

    evt.waitUntil(
        caches.open(CACHE_NAME).then((cache) => {
            console.log('[ServiceWorker] Pre-caching offline page');
            return cache.addAll(FILES_TO_CACHE);
        })
    );

    self.skipWaiting();
});

self.addEventListener('push', function(event) {
    const data = event.data.json()
    const options = {
        tag: data.title,
        icon: '/cinema-maskable-icon.png',
        badge: '/movie-transparent-icon.png',
        body: data.message,
        vibrate: [100, 50, 100],
        data: {
            dateOfArrival: Date.now(),
            primaryKey: '2'
        },
    };
    event.waitUntil(
        self.registration.showNotification('Rodada de Filmes', options)
    );
});

self.addEventListener('activate', (evt) => {
    console.log('[ServiceWorker] Activate');
    // Remove previous cached data from disk.
    evt.waitUntil(
        caches.keys().then((keyList) => {
            return Promise.all(keyList.map((key) => {
                if (key !== CACHE_NAME) {
                    console.log('[ServiceWorker] Removing old cache', key);
                    return caches.delete(key);
                }
            }));
        })
    );

    self.clients.claim();
});

self.addEventListener('fetch', function(event) {
    event.respondWith(
        caches.open(CACHE_NAME).then(function(cache) {
            return cache.match(event.request).then(function (response) {
                return response || fetch(event.request).then(function(response) {
                    cache.put(event.request, response.clone());
                    return response;
                });
            });
        })
    );
});