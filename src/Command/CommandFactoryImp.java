package Presentacion.Command;

import Presentacion.Command.Cliente.AltaClienteCommand;
import Presentacion.Command.Cliente.BajaClienteCommand;
import Presentacion.Command.Cliente.ListarClientesCommand;
import Presentacion.Command.Cliente.ListarClientesNormalCommand;
import Presentacion.Command.Cliente.ListarClientesVIPCommand;
import Presentacion.Command.Cliente.ModificarClienteCommand;
import Presentacion.Command.Cliente.MostrarClienteCommand;
import Presentacion.Command.Cliente.MostrarClienteParaModificarCommand;
import Presentacion.Command.Compra.CalcularTotalComprasPorPeliculaCommand;
import Presentacion.Command.Compra.CerrarCompraCommand;
import Presentacion.Command.Compra.DevolucionCompraCommand;
import Presentacion.Command.Compra.ListarComprasCommand;
import Presentacion.Command.Compra.ListarComprasPorClienteCommand;
import Presentacion.Command.Compra.ListarComprasPorPaseCommand;
import Presentacion.Command.Compra.MostrarCompraCommand;
import Presentacion.Command.Compra.MostrarCompraEnDetalleCommand;
import Presentacion.Command.Compra.VerResumenDeCompraCommand;
import Presentacion.Command.Pase.AltaPaseCommand;
import Presentacion.Command.Pase.BajaPaseCommand;
import Presentacion.Command.Pase.ListarPasesCommand;
import Presentacion.Command.Pase.ListarPasesPorCompraCommand;
import Presentacion.Command.Pase.ListarPasesPorPeliculaCommand;
import Presentacion.Command.Pase.ListarPasesPorSalaCommand;
import Presentacion.Command.Pase.ModificarPaseCommand;
import Presentacion.Command.Pase.MostrarPaseCommand;
import Presentacion.Command.Pase.MostrarPaseParaModificarCommand;
import Presentacion.Command.Pelicula.AltaPeliculaCommand;
import Presentacion.Command.Pelicula.AnadirProductoraCommand;
import Presentacion.Command.Pelicula.BajaPeliculaCommand;
import Presentacion.Command.Pelicula.ListarPeliculasCommand;
import Presentacion.Command.Pelicula.ListarPeliculasPorProductoraCommand;
import Presentacion.Command.Pelicula.ModificarPeliculaCommand;
import Presentacion.Command.Pelicula.MostrarPeliculaCommand;
import Presentacion.Command.Pelicula.MostrarPeliculaParaModificarCommand;
import Presentacion.Command.Pelicula.QuitarProductoraCommand;
import Presentacion.Command.Productora.AltaProductoraCommand;
import Presentacion.Command.Productora.BajaProductoraCommand;
import Presentacion.Command.Productora.CalcularProductoraMasIngresosCommand;
import Presentacion.Command.Productora.ListarProductoraCommand;
import Presentacion.Command.Productora.ListarProductorasPorPeliculaCommand;
import Presentacion.Command.Productora.ModificarProductoraCommand;
import Presentacion.Command.Productora.MostrarProductoraCommand;
import Presentacion.Command.Productora.MostrarProductoraParaModificarCommand;
import Presentacion.Command.Sala.AltaSalaCommand;
import Presentacion.Command.Sala.BajaSalaCommand;
import Presentacion.Command.Sala.ListarSalasCommand;
import Presentacion.Command.Sala.ListarSalasEspecialCommand;
import Presentacion.Command.Sala.ListarSalasEstandarCommand;
import Presentacion.Command.Sala.ModificarSalaCommand;
import Presentacion.Command.Sala.MostrarSalaCommand;
import Presentacion.Command.Sala.MostrarSalaParaModificarCommand;
import Presentacion.Controller.ContextEnum;

public class CommandFactoryImp extends CommandFactory {
	
	@Override
	public Command GetCommand(ContextEnum contextEnum) {
		
		if (map.containsKey(contextEnum))
			return map.get(contextEnum);
		
		switch(contextEnum){
		case ALTACLIENTE:
			map.put(ContextEnum.ALTACLIENTE, new AltaClienteCommand());
			break;
		case BAJACLIENTE:
			map.put(ContextEnum.BAJACLIENTE, new BajaClienteCommand());
			break;
		case MODIFICARCLIENTE:
			map.put(ContextEnum.MODIFICARCLIENTE, new ModificarClienteCommand());
			break;
		case MOSTRARCLIENTE:
			map.put(ContextEnum.MOSTRARCLIENTE, new MostrarClienteCommand());
			break;
		case LISTARCLIENTES:
			map.put(ContextEnum.LISTARCLIENTES, new ListarClientesCommand());
			break;
		case LISTARCLIENTESNORMAL:
			map.put(ContextEnum.LISTARCLIENTESNORMAL, new ListarClientesNormalCommand());
			break;
		case LISTARCLIENTESVIP:
			map.put(ContextEnum.LISTARCLIENTESVIP, new ListarClientesVIPCommand());
			break;
		case ALTAPASE:
			map.put(ContextEnum.ALTAPASE, new AltaPaseCommand());
			break;
		case BAJAPASE:
			map.put(ContextEnum.BAJAPASE, new BajaPaseCommand());
			break;
		case MODIFICARPASE:
			map.put(ContextEnum.MODIFICARPASE, new ModificarPaseCommand());
			break;
		case MOSTRARPASE:
			map.put(ContextEnum.MOSTRARPASE, new MostrarPaseCommand());
			break;
		case LISTARPASES:
			map.put(ContextEnum.LISTARPASES, new ListarPasesCommand());
			break;
		case LISTARPASESPORSALA:
			map.put(ContextEnum.LISTARPASESPORSALA, new ListarPasesPorSalaCommand());
			break;
		case LISTARPASESPORPELICULA:
			map.put(ContextEnum.LISTARPASESPORPELICULA, new ListarPasesPorPeliculaCommand());
			break;
		case LISTARPASESPORCOMPRA:
			map.put(ContextEnum.LISTARPASESPORCOMPRA, new ListarPasesPorCompraCommand());
			break;	
		case ALTAPRODUCTORA:
			map.put(ContextEnum.ALTAPRODUCTORA, new AltaProductoraCommand());
			break;
		case ALTAPELICULA:
			map.put(ContextEnum.ALTAPELICULA,  new AltaPeliculaCommand());
			break;
		case ALTASALA:
			map.put(ContextEnum.ALTASALA, new AltaSalaCommand());
			break;
		case ANADIRPRODUCTORA:
			map.put(ContextEnum.ANADIRPRODUCTORA, new AnadirProductoraCommand());
			break;
		case BAJAPELICULA:
			map.put(ContextEnum.BAJAPELICULA, new BajaPeliculaCommand());
			break;
		case BAJAPRODUCTORA:
			map.put(ContextEnum.BAJAPRODUCTORA, new BajaProductoraCommand());
			break;
		case BAJASALA:
			map.put(ContextEnum.BAJASALA, new BajaSalaCommand());
			break;
		case CALCULARPRODUCTORAMASINGRESOS:
			map.put(ContextEnum.CALCULARPRODUCTORAMASINGRESOS, new CalcularProductoraMasIngresosCommand());
			break;
		case CALCULARTOTALCOMPRASPORPELICULA:
			map.put(ContextEnum.CALCULARTOTALCOMPRASPORPELICULA, new CalcularTotalComprasPorPeliculaCommand());
			break;
		case CERRARCOMPRA:
			map.put(ContextEnum.CERRARCOMPRA, new CerrarCompraCommand());
			break;
		case DEVOLVERCOMPRA:
			map.put(ContextEnum.DEVOLVERCOMPRA, new DevolucionCompraCommand());
			break;
		case LISTARCOMPRAS:
			map.put(ContextEnum.LISTARCOMPRAS, new ListarComprasCommand());
			break;
		case LISTARCOMPRASPORCLIENTE:
			map.put(ContextEnum.LISTARCOMPRASPORCLIENTE, new ListarComprasPorClienteCommand());
			break;
		case LISTARCOMPRASPORPASE:
			map.put(ContextEnum.LISTARCOMPRASPORPASE, new ListarComprasPorPaseCommand());
			break;
		case LISTARPELICULAS:
			map.put(ContextEnum.LISTARPELICULAS, new ListarPeliculasCommand());
			break;
		case LISTARPELICULASPORPRODUCTORA:
			map.put(ContextEnum.LISTARPELICULASPORPRODUCTORA, new ListarPeliculasPorProductoraCommand());
			break;
		case LISTARPRODUCTORAS:
			map.put(ContextEnum.LISTARPRODUCTORAS, new ListarProductoraCommand());
			break;
		case LISTARPRODUCTORASPORPELICULA:
			map.put(ContextEnum.LISTARPRODUCTORASPORPELICULA, new ListarProductorasPorPeliculaCommand());
			break;
		case LISTARSALAS:
			map.put(ContextEnum.LISTARSALAS, new ListarSalasCommand());
			break;
		case LISTARSALASESPECIALES:
			map.put(ContextEnum.LISTARSALASESPECIALES, new ListarSalasEspecialCommand());
			break;
		case LISTARSALASESTANDAR:
			map.put(ContextEnum.LISTARSALASESTANDAR, new ListarSalasEstandarCommand());
			break;
		case MODIFICARPELICULA:
			map.put(ContextEnum.MODIFICARPELICULA, new ModificarPeliculaCommand());
			break;
		case MODIFICARPRODUCTORA:
			map.put(ContextEnum.MODIFICARPRODUCTORA, new ModificarProductoraCommand());
			break;
		case MOSTRARPRODUCTORAPARAMODIFICAR:
			map.put(ContextEnum.MOSTRARPRODUCTORAPARAMODIFICAR, new MostrarProductoraParaModificarCommand());
			break;
		case MOSTRARCLIENTEPARAMODIFICAR:
			map.put(ContextEnum.MOSTRARCLIENTEPARAMODIFICAR, new MostrarClienteParaModificarCommand());
			break;
		case MOSTRARPASEPARAMODIFICAR:
			map.put(ContextEnum.MOSTRARPASEPARAMODIFICAR, new MostrarPaseParaModificarCommand());
			break;
		case MOSTRARPELICULAPARAMODIFICAR:
			map.put(ContextEnum.MOSTRARPELICULAPARAMODIFICAR, new MostrarPeliculaParaModificarCommand());
			break;
		case MOSTRARSALAPARAMODIFICAR:
			map.put(ContextEnum.MOSTRARSALAPARAMODIFICAR, new MostrarSalaParaModificarCommand());
			break;
		case MODIFICARSALA:
			map.put(ContextEnum.MODIFICARSALA, new ModificarSalaCommand());
			break;
		case MOSTRARCOMPRA:
			map.put(ContextEnum.MOSTRARCOMPRA, new MostrarCompraCommand());
			break;
		case MOSTRARCOMPRAENDETALLE:
			map.put(ContextEnum.MOSTRARCOMPRAENDETALLE, new MostrarCompraEnDetalleCommand());
			break;
		case MOSTRARPELICULA:
			map.put(ContextEnum.MOSTRARPELICULA, new MostrarPeliculaCommand());
			break;
		case MOSTRARPRODUCTORA:
			map.put(ContextEnum.MOSTRARPRODUCTORA, new MostrarProductoraCommand());
			break;
		case MOSTRARSALA:
			map.put(ContextEnum.MOSTRARSALA, new MostrarSalaCommand());
			break;
		case QUITARPRODUCTORA:
			map.put(ContextEnum.QUITARPRODUCTORA, new QuitarProductoraCommand());
			break;
		case VERRESUMENDECOMPRA:
			map.put(ContextEnum.VERRESUMENDECOMPRA, new VerResumenDeCompraCommand());
			break;
		default:
			break;
		}
		return map.get(contextEnum);
	}
	
}