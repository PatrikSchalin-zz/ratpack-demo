package com.syve.ratpack.demo

import com.google.common.io.Resources
import groovy.util.logging.Slf4j
import ratpack.service.Service
import ratpack.service.StartEvent
import ratpack.service.StopEvent
import ratpack.util.RatpackVersion

@Slf4j
class Banner implements Service {

    @Override
    void onStart(StartEvent event) {
        log.info('Starting Ratpack Server')
        showBanner()
    }

    @Override
    void onStop(StopEvent event) {
        log.info('Stopping Ratpack Server')
    }

    private void showBanner() {
        final URL bannerResource = Resources.getResource('banner.txt')
        final String banner = bannerResource.text

        println banner
        println ":: Ratpack Version => ${RatpackVersion.version} ::"
        println ":: Java Version    => ${System.getProperty('java.version')} ::"
    }
}