package com.tienda.bean;

import com.tienda.modelo.Producto;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class ProductoBean implements Serializable {

    @PersistenceContext(unitName = "tiendaPU")
    private EntityManager em;

    private List<Producto> productos;
    private Producto productoSeleccionado;

    @PostConstruct
    public void init() {
        this.productos = em.createQuery("SELECT p FROM Producto p ORDER BY p.nombre", Producto.class).getResultList();
        this.productoSeleccionado = new Producto();
    }

    @Transactional
    public void guardar() {
        String summary;
        if (this.productoSeleccionado.getId() == null) {
            em.persist(this.productoSeleccionado);
            summary = "Producto Creado";
        } else {
            em.merge(this.productoSeleccionado);
            summary = "Producto Actualizado";
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", summary));

        init();
    }

    @Transactional
    public void eliminar(Producto producto) {
        em.remove(em.merge(producto));

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto Eliminado"));

        init();
    }

    public void prepararNuevo() {
        this.productoSeleccionado = new Producto();
    }

    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
    public Producto getProductoSeleccionado() { return productoSeleccionado; }
    public void setProductoSeleccionado(Producto productoSeleccionado) { this.productoSeleccionado = productoSeleccionado; }
}