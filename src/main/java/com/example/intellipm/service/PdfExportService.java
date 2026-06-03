package com.example.intellipm.service;

import com.example.intellipm.dto.ReportDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import com.example.intellipm.exception.ResourceNotFoundException;

@Service
public class PdfExportService {

    public byte[] genererRapportPdf(ReportDTO report) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font sectionFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 12);

            Paragraph title = new Paragraph("Rapport d'avancement du projet", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Informations du projet", sectionFont));
            document.add(new Paragraph("ID Projet : " + report.getProjectId(), normalFont));
            document.add(new Paragraph("Titre : " + report.getTitreProjet(), normalFont));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Statistiques des tâches", sectionFont));
            document.add(new Paragraph("Total des tâches : " + report.getTotalTaches(), normalFont));
            document.add(new Paragraph("Tâches terminées : " + report.getTachesTerminees(), normalFont));
            document.add(new Paragraph("Tâches en cours : " + report.getTachesEnCours(), normalFont));
            document.add(new Paragraph("Tâches en retard : " + report.getTachesEnRetard(), normalFont));
            document.add(new Paragraph("Taux d'avancement : " + report.getTauxAvancement() + " %", normalFont));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Résumé", sectionFont));

            String resume = "Le projet \"" + report.getTitreProjet() + "\" contient "
                    + report.getTotalTaches() + " tâche(s). "
                    + report.getTachesTerminees() + " tâche(s) sont terminées, "
                    + report.getTachesEnCours() + " tâche(s) sont en cours, et "
                    + report.getTachesEnRetard() + " tâche(s) sont en retard.";

            document.add(new Paragraph(resume, normalFont));

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }
}