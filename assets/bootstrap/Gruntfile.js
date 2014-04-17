var path = require('path');

module.exports = function(grunt) {
  grunt.initConfig({
    watch: {
      less: {
          files: [
                  'less/style.less'
          ],
          tasks: ['less:development']
      }
    },
    uglify: {
      dist: {
        files: {
          'js/core.min.js': [
            'vendor/jquery/dist/jquery.js',
            'vendor/bootstrap/js/affix.js',
            'vendor/bootstrap/js/alert.js',
            'vendor/bootstrap/js/button.js',
            'vendor/bootstrap/js/carousel.js',
            'vendor/bootstrap/js/collapse.js',
            'vendor/bootstrap/js/dropdown.js',
            'vendor/bootstrap/js/modal.js',
            'vendor/bootstrap/js/tooltip.js',
            'vendor/bootstrap/js/popover.js',
            'vendor/bootstrap/js/scrollspy.js',
            'vendor/bootstrap/js/tab.js',
            'vendor/bootstrap/js/transition.js',
            'vendor/jquery-tmpl/jquery.tmpl.js'
          ]
        }
      }
    },
    less: {
      dist: {
          options: {
              cleancss: true
          },
          files: {
              'css/core.min.css': [
                  'less/style.less'
              ]
          }
      }
    },
    concat: {
        css: {
            src: [
                 'vendor/bootstrap/dist/css/bootstrap.css',
                 'css/style.min.css'
            ],
            dest: 'css/all.css'
        }
    },
    cssmin: {
            css: {
                src: 'css/all.css',
                dest: 'css/core.min.css'
            }
    }
  });

  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-cssmin');
  grunt.loadNpmTasks('grunt-contrib-concat');

  grunt.registerTask('build', ['uglify', 'less', 'concat', 'cssmin']);
};
