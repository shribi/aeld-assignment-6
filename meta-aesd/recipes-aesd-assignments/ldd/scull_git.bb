LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit module update-rc.d

SRC_URI = "git://github.com/shribi/aeld-assignment-7;protocol=ssh;branch=master \
           file://ldd-start-stop.sh \
          "
SRCREV = "7d2f02c6b0908991ffdc6202f89766b97d990fe2"
PV = "1.0+git${SRCPV}"

S = "${WORKDIR}/git"

# Wildcard avoids needing KERNEL_VERSION resolved at parse time
FILES:${PN} += "${nonarch_base_libdir}/modules/*/ldd/*.ko ${sysconfdir}/init.d/ldd-start-stop.sh"

INITSCRIPT_PACKAGE = "${PN}"
INITSCRIPT_NAME = "ldd-start-stop.sh"

# module_do_compile unsets CFLAGS, so pass the include dir via KCFLAGS instead
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR} M=${S}/scull -C ${STAGING_KERNEL_DIR} KCFLAGS=-I${S}/include"

do_install:append() {
	install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/ldd
	install -m 0644 ${S}/scull/*.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/ldd/

	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/ldd-start-stop.sh ${D}${sysconfdir}/init.d/ldd-start-stop.sh
}