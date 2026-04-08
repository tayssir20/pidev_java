package org.example.test;

import org.example.entities.Blog;
import org.example.services.ServiceBlog;
import org.example.utils.MyDatabase;

public class MainBlog {
    public static void main(String[] args) {
        MyDatabase.getInstance();

        ServiceBlog serviceBlog = new ServiceBlog();

        Blog blog = new Blog("Premier blog", "Contenu du blog", "General", "blog.png");
        serviceBlog.ajouter(blog);

        serviceBlog.afficher().forEach(System.out::println);

        if (!serviceBlog.afficher().isEmpty()) {
            Blog firstBlog = serviceBlog.afficher().get(0);
            firstBlog.setTitle("Premier blog modifie");
            firstBlog.setContent("Contenu mis a jour");
            serviceBlog.modifier(firstBlog);
            System.out.println(serviceBlog.afficher());
            serviceBlog.supprimer(firstBlog.getId());
        }
    }
}