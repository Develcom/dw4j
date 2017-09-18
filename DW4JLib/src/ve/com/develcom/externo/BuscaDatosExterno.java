package ve.com.develcom.externo;

import ve.com.develcom.servicios.DatosExternosServicios;


public class BuscaDatosExterno {
    
    public java.util.List<com.develcom.externo.Indice> encontrarDatosExternos(java.lang.String cedula, int idCategoria) {
        return buscarDatosExternos(cedula, idCategoria);
    }

    private static java.util.List<com.develcom.externo.Indice> buscarDatosExternos(java.lang.String cedula, int idCategoria) {
        DatosExternosServicios service = new DatosExternosServicios();
//        com.develcom.externo.DatosExternos_Service service = new com.develcom.externo.DatosExternos_Service();
        com.develcom.externo.DatosExternos port = service.getDatosExternosPort();
        return port.buscarDatosExternos(cedula, idCategoria);
    }
    
}
