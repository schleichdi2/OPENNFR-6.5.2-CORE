inherit qmake5_paths

PACKAGECONFIG[qt5] = '-Dqt5=enabled,-Dqt5=disabled,qtbase qtdeclarative qtbase-native'

# The GStreamer Qt5 plugin needs desktop OpenGL or OpenGL ES to work, so make sure it is enabled
python() {
    cur_packageconfig = d.getVar('PACKAGECONFIG',True).split()
    if 'qt5' in cur_packageconfig and not (('opengl' in cur_packageconfig) or ('gles2' in cur_packageconfig)):
        gl_packageconfig = d.getVar('PACKAGECONFIG_GL',True)
        d.appendVar('PACKAGECONFIG', ' ' + gl_packageconfig)
}
