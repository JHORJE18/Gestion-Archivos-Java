package gestionficherosapp;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.text.SimpleDateFormat;

import gestionficheros.FormatoVistas;
import gestionficheros.GestionFicheros;
import gestionficheros.GestionFicherosException;
import gestionficheros.TipoOrden;

public class GestionFicherosImpl implements GestionFicheros {
	private File carpetaDeTrabajo = null;
	private Object[][] contenido;
	private int filas = 0;
	private int columnas = 3;
	private FormatoVistas formatoVistas = FormatoVistas.NOMBRES;
	private TipoOrden ordenado = TipoOrden.DESORDENADO;

	public GestionFicherosImpl() {
		carpetaDeTrabajo = File.listRoots()[0];
		actualiza();
	}

	private void actualiza() {

		String[] ficheros = carpetaDeTrabajo.list(); // obtener los nombres
		// calcular el n˙mero de filas necesario
		filas = ficheros.length / columnas;
		if (filas * columnas < ficheros.length) {
			filas++; // si hay resto necesitamos una fila m·s
		}

		// dimensionar la matriz contenido seg˙n los resultados

		contenido = new String[filas][columnas];
		// Rellenar contenido con los nombres obtenidos
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				int ind = j * columnas + i;
				if (ind < ficheros.length) {
					contenido[j][i] = ficheros[ind];
				} else {
					contenido[j][i] = "";
				}
			}
		}
	}

	@Override
	public void arriba() {

		System.out.println("holaaa");
		if (carpetaDeTrabajo.getParentFile() != null) {
			carpetaDeTrabajo = carpetaDeTrabajo.getParentFile();
			actualiza();
		}

	}

	@Override
	public void creaCarpeta(String arg0) throws GestionFicherosException {
		File folder = new File(carpetaDeTrabajo,"");
		File file = new File(carpetaDeTrabajo,arg0);
		
		//que se pueda escribir -> lanzar· una excepciÛn
		if (!folder.canWrite()){
			throw new GestionFicherosException("No cuenta con permiso de escritura en el directorio " + folder);
		}
		
		//que no exista -> lanzar· una excepciÛn
		if (file.exists()){
			throw new GestionFicherosException("La carpeta ya existe");
		}
		
		//crear la carpeta -> lanzar· una excepciÛn
		if (file.mkdirs()){
			System.out.println("Se ha creado la carpeta " + arg0);
		} else {
			throw new GestionFicherosException("No se ha podido crear la carpeta " + arg0);
		}
		
		actualiza();
	}

	@Override
	public void creaFichero(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		//File folder = new File(carpetaDeTrabajo,"");
		File file = new File(carpetaDeTrabajo,arg0);
		
		//No se puede escribir -> lanzar· una excepciÛn
		if (!carpetaDeTrabajo.canWrite()){
			throw new GestionFicherosException("No cuenta con permiso de escritura en el directorio " + carpetaDeTrabajo);
		}
		
		//No existe -> lanzar· una excepciÛn
		if (file.exists()){
			throw new GestionFicherosException("El archivo ya existe");
		}
		
		//Crear el archivo -> lanzar· una excepciÛn
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new GestionFicherosException("No se ha podido crear el archivo " + arg0);
		}
		
		actualiza();
	}
	

	@Override
	public void elimina(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
			File file = new File(carpetaDeTrabajo,arg0);
			
			//Permiso de mod
			if (!carpetaDeTrabajo.canWrite()){
				throw new GestionFicherosException("No tienes permisos para borrar " + arg0);
			}
			
			//Existencia
			if (!file.exists()){
				throw new GestionFicherosException("No existe el archivo " + arg0);
			}
			
			//Elimina archivo
			if (file.delete()){
				System.out.println("Se ha borrado " + arg0 + " hijo de puta");
			} else {
				throw new GestionFicherosException("No se ha podido borrar " + arg0);
			}
			
			actualiza();
	}

	@Override
	public void entraA(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		// se controla que el nombre corresponda a una carpeta existente
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se ha encontrado "
					+ file.getAbsolutePath()
					+ " pero se esperaba un directorio");
		}
		// se controla que se tengan permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException("Alerta. No se puede acceder a "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		// nueva asignaciÛn de la carpeta de trabajo
		carpetaDeTrabajo = file;
		// se requiere actualizar contenido
		actualiza();

	}

	@Override
	public int getColumnas() {
		return columnas;
	}

	@Override
	public Object[][] getContenido() {
		return contenido;
	}

	@Override
	public String getDireccionCarpeta() {
		return carpetaDeTrabajo.getAbsolutePath();
	}

	@Override
	public String getEspacioDisponibleCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEspacioTotalCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilas() {
		return filas;
	}

	@Override
	public FormatoVistas getFormatoContenido() {
		return formatoVistas;
	}

	@Override
	public String getInformacion(String arg0) throws GestionFicherosException {
		
		StringBuilder strBuilder=new StringBuilder();
		File file = new File(carpetaDeTrabajo,arg0);
		
		//Controlar que existe. Si no, se lanzar· una excepciÛn
		strBuilder.append("Existencia archivo: ");
		if (file.exists()){
			strBuilder.append("Correcto \n");
		}	else	{
			throw new GestionFicherosException("No existe el fichero");
		}
		
		//Controlar que haya permisos de lectura. Si no, se lanzar· una excepciÛn
		strBuilder.append("Permiso de lectura: ");
		if (file.canRead()){
			strBuilder.append(" Correcto \n");
		} else {
			throw new GestionFicherosException("No hay permiso");
		}
		
		//TÌtulo
		strBuilder.append("INFORMACIÓN DEL SISTEMA");
		strBuilder.append("\n\n");
		
		//Nombre
		strBuilder.append("Nombre: ");
		strBuilder.append(arg0);
		strBuilder.append("\n");
		
		//Tipo: fichero o directorio
		strBuilder.append("Tipo: ");
		if (file.isFile()){
			strBuilder.append("Archivo \n");
		} else if (file.isDirectory()){
			strBuilder.append("Directorio \n");
		}
		
		//UbicaciÛn
		strBuilder.append("Ubicación: ");
		strBuilder.append(file.getPath() + "\n");
		
		//Fecha de ˙ltima modificaciÛn
		strBuilder.append("Última modificación: ");
		
			//Formatear fecha y hora obtenido
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		strBuilder.append(sdf.format(file.lastModified()) + "\n");
		
		//Si es un fichero oculto o no
		if (file.isHidden()){
			strBuilder.append("Fichero no visible por el Sistema \n");
		} else {
			strBuilder.append("Fichero visible por el Sistema \n");
		}
		
		//Si es directorio: Espacio libre, espacio disponible, espacio total
		//bytes
		if (file.isDirectory()){
			//Calculos espacio
			strBuilder.append("Espacio libre: " + file.getFreeSpace() + " bytes \n");
			strBuilder.append("Espacio dispondible: " + file.getUsableSpace() + " bytes \n");
			strBuilder.append("Espacio total: " + file.getTotalSpace() + " bytes \n");
			
			//Archivos en el directorio
			strBuilder.append("Archivos en el directorio: " + file.list().length + "\n");
		}	else if (file.isFile()) {
			//Mostrar tamaño file
			strBuilder.append("Tamaño archivo: " + file.length() + " bytes \n");
		}
		
		return strBuilder.toString();
	}

	@Override
	public boolean getMostrarOcultos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNombreCarpeta() {
		return carpetaDeTrabajo.getName();
	}

	@Override
	public TipoOrden getOrdenado() {
		return ordenado;
	}

	@Override
	public String[] getTituloColumnas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getUltimaModificacion(String arg0)
			throws GestionFicherosException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String nomRaiz(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numRaices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renombra(String arg0, String arg1)	throws GestionFicherosException {
		
		//Antiguo File
		File file = new File(carpetaDeTrabajo,arg0);

		//Nuevo File
		File fileNEW = new File(carpetaDeTrabajo,arg1);
		
		//Permiso
		if (!carpetaDeTrabajo.canRead()){
			throw new GestionFicherosException("No tienes permisos");
		}
		
		//Existe
		if (fileNEW.exists()){
			throw new GestionFicherosException("Ya existe un archivo/directorio igual");
		}
		
		//Renombra
		if (file.renameTo(fileNEW)){
			System.out.println("Se ha renombrado al antiguo " + arg0 + " a " + arg1);
		} else {
			throw new GestionFicherosException("Error renombrando archivos");
		}
		
		actualiza();
	}

	@Override
	public boolean sePuedeEjecutar(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeEscribir(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeLeer(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setColumnas(int arg0) {
		columnas = arg0;

	}

	@Override
	public void setDirCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(arg0);

		// se controla que la direcciÛn exista y sea directorio
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se esperaba "
					+ "un directorio, pero " + file.getAbsolutePath()
					+ " no es un directorio.");
		}

		// se controla que haya permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException(
					"Alerta. No se puede acceder a  " + file.getAbsolutePath()
							+ ". No hay permisos");
		}

		// actualizar la carpeta de trabajo
		carpetaDeTrabajo = file;

		// actualizar el contenido
		actualiza();

	}

	@Override
	public void setFormatoContenido(FormatoVistas arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMostrarOcultos(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOrdenado(TipoOrden arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEjecutar(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEscribir(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeLeer(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUltimaModificacion(String arg0, long arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

}
