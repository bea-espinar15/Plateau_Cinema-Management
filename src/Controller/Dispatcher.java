package Presentacion.Controller;

import Presentacion.Gui.Panels.Cliente.AltaClientePanel;
import Presentacion.Gui.Panels.Cliente.BajaClientePanel;
import Presentacion.Gui.Panels.Cliente.ListarClientesNormalPanel;
import Presentacion.Gui.Panels.Cliente.ListarClientesPanel;
import Presentacion.Gui.Panels.Cliente.ListarClientesVIPPanel;
import Presentacion.Gui.Panels.Cliente.ModificarClientePanel;
import Presentacion.Gui.Panels.Cliente.MostrarClientePanel;
import Presentacion.Gui.Panels.Compra.CalcularTotalComprasPorPeliculaPanel;
import Presentacion.Gui.Panels.Compra.CerrarCompraPanel;
import Presentacion.Gui.Panels.Compra.DevolverCompraPanel;
import Presentacion.Gui.Panels.Compra.ListarComprasPanel;
import Presentacion.Gui.Panels.Compra.ListarComprasPorClientePanel;
import Presentacion.Gui.Panels.Compra.ListarComprasPorPasePanel;
import Presentacion.Gui.Panels.Compra.MostrarCompraEnDetallePanel;
import Presentacion.Gui.Panels.Compra.MostrarCompraPanel;
import Presentacion.Gui.Panels.Compra.VerResumenDeCompraPanel;
import Presentacion.Gui.Panels.Pase.AltaPasePanel;
import Presentacion.Gui.Panels.Pase.BajaPasePanel;
import Presentacion.Gui.Panels.Pase.ListarPasesPanel;
import Presentacion.Gui.Panels.Pase.ListarPorCompraPanel;
import Presentacion.Gui.Panels.Pase.ListarPorPeliculaPanel;
import Presentacion.Gui.Panels.Pase.ListarPorSalaPanel;
import Presentacion.Gui.Panels.Pase.ModificarPasePanel;
import Presentacion.Gui.Panels.Pase.MostrarPasePanel;
import Presentacion.Gui.Panels.Pelicula.AltaPeliculaPanel;
import Presentacion.Gui.Panels.Pelicula.AnadirProductoraPanel;
import Presentacion.Gui.Panels.Pelicula.BajaPeliculaPanel;
import Presentacion.Gui.Panels.Pelicula.ListarPeliculaPanel;
import Presentacion.Gui.Panels.Pelicula.ListarPorProductoraPanel;
import Presentacion.Gui.Panels.Pelicula.ModificarPeliculaPanel;
import Presentacion.Gui.Panels.Pelicula.MostrarPeliculaPanel;
import Presentacion.Gui.Panels.Pelicula.QuitarProductoraPanel;
import Presentacion.Gui.Panels.Productora.AltaProductoraPanel;
import Presentacion.Gui.Panels.Productora.BajaProductoraPanel;
import Presentacion.Gui.Panels.Productora.CalcularProductoraMasIngresosPanel;
import Presentacion.Gui.Panels.Productora.ListarProductorasPanel;
import Presentacion.Gui.Panels.Productora.ModificarProductoraPanel;
import Presentacion.Gui.Panels.Productora.MostrarProductoraPanel;
import Presentacion.Gui.Panels.Sala.AltaSalaPanel;
import Presentacion.Gui.Panels.Sala.BajaSalaPanel;
import Presentacion.Gui.Panels.Sala.ListarSalasEspecialPanel;
import Presentacion.Gui.Panels.Sala.ListarSalasEstandarPanel;
import Presentacion.Gui.Panels.Sala.ListarSalasPanel;
import Presentacion.Gui.Panels.Sala.ModificarSalaPanel;
import Presentacion.Gui.Panels.Sala.MostrarSalaPanel;

public class Dispatcher{
	
	private static Dispatcher instance;
		
	public static Dispatcher getInstance() {
		if(instance == null){
			instance = new Dispatcher();
		}
		return instance;
	}
	
	public void Dispatch(Context response) {
		switch(response.getContext()){
		case ALTACLIENTE:
			AltaClientePanel.getInstance().update(response.getData());
			break;
		case ALTAPASE:
			AltaPasePanel.getInstance().update(response.getData());
			break;
		case ALTAPELICULA:
			AltaPeliculaPanel.getInstance().update(response.getData());
			break;
		case ALTAPRODUCTORA:
			AltaProductoraPanel.getInstance().update(response.getData());
			break;
		case ALTASALA:
			AltaSalaPanel.getInstance().update(response.getData());
			break;
		case ANADIRPRODUCTORA:
			AnadirProductoraPanel.getInstance().update(response.getData());;
			break;
		case BAJACLIENTE:
			BajaClientePanel.getInstance().update(response.getData());
			break;
		case BAJAPASE:
			BajaPasePanel.getInstance().update(response.getData());
			break;
		case BAJAPELICULA:
			BajaPeliculaPanel.getInstance().update(response.getData());
			break;
		case BAJAPRODUCTORA:
			BajaProductoraPanel.getInstance().update(response.getData());
			break;
		case BAJASALA:
			BajaSalaPanel.getInstance().update(response.getData());
			break;
		case CALCULARPRODUCTORAMASINGRESOS:
			CalcularProductoraMasIngresosPanel.getInstance().update(response.getData());
			break;
		case CALCULARTOTALCOMPRASPORPELICULA:
			CalcularTotalComprasPorPeliculaPanel.getInstance().update(response.getData());
			break;
		case CERRARCOMPRA:
			CerrarCompraPanel.getInstance().update(response.getData());
			break;
		case DEVOLVERCOMPRA:
			DevolverCompraPanel.getInstance().update(response.getData());
			break;
		case LISTARCLIENTES:
			ListarClientesPanel.getInstance().update(response.getData());
			break;
		case LISTARCLIENTESNORMAL:
			ListarClientesNormalPanel.getInstance().update(response.getData());
			break;
		case LISTARCLIENTESVIP:
			ListarClientesVIPPanel.getInstance().update(response.getData());
			break;
		case LISTARCOMPRAS:
			ListarComprasPanel.getInstance().update(response.getData());
			break;
		case LISTARCOMPRASPORCLIENTE:
			ListarComprasPorClientePanel.getInstance().update(response.getData());
			break;
		case LISTARCOMPRASPORPASE:
			ListarComprasPorPasePanel.getInstance().update(response.getData());
			break;
		case LISTARPASES:
			ListarPasesPanel.getInstance().update(response.getData());
			break;
		case LISTARPASESPORCOMPRA:
			ListarPorCompraPanel.getInstance().update(response.getData());
			break;
		case LISTARPASESPORPELICULA: 
			Presentacion.Gui.Panels.Pase.ListarPorPeliculaPanel.getInstance().update(response.getData());
			break;
		case LISTARPASESPORSALA:
			ListarPorSalaPanel.getInstance().update(response.getData());
			break;
		case LISTARPELICULAS:
			ListarPeliculaPanel.getInstance().update(response.getData());
			break;
		case LISTARPELICULASPORPRODUCTORA:
			ListarPorProductoraPanel.getInstance().update(response.getData());
			break;
		case LISTARPRODUCTORAS:
			ListarProductorasPanel.getInstance().update(response.getData());
			break;
		case LISTARPRODUCTORASPORPELICULA:
			Presentacion.Gui.Panels.Productora.ListarPorPeliculaPanel.getInstance().update(response.getData());//NO TOCAR
			break;
		case LISTARSALAS:
			ListarSalasPanel.getInstance().update(response.getData());
			break;
		case LISTARSALASESPECIALES:
			ListarSalasEspecialPanel.getInstance().update(response.getData());
			break;
		case LISTARSALASESTANDAR:
			ListarSalasEstandarPanel.getInstance().update(response.getData());
			break;
		case MOSTRARCLIENTEPARAMODIFICAR:
			ModificarClientePanel.getInstance().update(response.getData());
			break;
		case MODIFICARCLIENTE:
			ModificarClientePanel.getInstance().update(response.getData());
			break;
		case MOSTRARPASEPARAMODIFICAR:
			ModificarPasePanel.getInstance().update(response.getData());
			break;
		case MODIFICARPASE:
			ModificarPasePanel.getInstance().update(response.getData());
			break;
		case MOSTRARPELICULAPARAMODIFICAR:
			ModificarPeliculaPanel.getInstance().update(response.getData());
			break;
		case MODIFICARPELICULA:
			ModificarPeliculaPanel.getInstance().update(response.getData());
			break;
		case MOSTRARPRODUCTORAPARAMODIFICAR:
			ModificarProductoraPanel.getInstance().update(response.getData());
			break;
		case MODIFICARPRODUCTORA:
			ModificarProductoraPanel.getInstance().update(response.getData());
			break;
		case MOSTRARSALAPARAMODIFICAR:
			ModificarSalaPanel.getInstance().update(response.getData());
			break;
		case MODIFICARSALA:
			ModificarSalaPanel.getInstance().update(response.getData());
			break;
		case MOSTRARCLIENTE:
			MostrarClientePanel.getInstance().update(response.getData());
			break;
		case MOSTRARCOMPRA:
			MostrarCompraPanel.getInstance().update(response.getData());
			break;
		case MOSTRARCOMPRAENDETALLE:
			MostrarCompraEnDetallePanel.getInstance().update(response.getData());
			break;
		case MOSTRARPASE:
			MostrarPasePanel.getInstance().update(response.getData());
			break;
		case MOSTRARPELICULA:
			MostrarPeliculaPanel.getInstance().update(response.getData());
			break;
		case MOSTRARPRODUCTORA:
			MostrarProductoraPanel.getInstance().update(response.getData());
			break;
		case MOSTRARSALA:
			MostrarSalaPanel.getInstance().update(response.getData());
			break;
		case QUITARPRODUCTORA:
			QuitarProductoraPanel.getInstance().update(response.getData());
			break;
		case VERRESUMENDECOMPRA:
			VerResumenDeCompraPanel.getInstance().update(response.getData());
			break;
		default:
			break;		
		}
	}
	
}
