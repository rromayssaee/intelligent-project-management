package com.example.intellipm.controller;

import com.example.intellipm.dto.ReportDTO;
import com.example.intellipm.service.PdfExportService;
import com.example.intellipm.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final PdfExportService pdfExportService;

    public ReportController(ReportService reportService, PdfExportService pdfExportService) {
        this.reportService = reportService;
        this.pdfExportService = pdfExportService;
    }

    @GetMapping("/project/{projectId}")
    public ReportDTO genererRapportProjet(@PathVariable Long projectId) {
        return reportService.genererRapportProjet(projectId);
    }

    @GetMapping("/project/{projectId}/pdf")
    public ResponseEntity<byte[]> genererRapportPdf(@PathVariable Long projectId) {
        ReportDTO report = reportService.genererRapportProjet(projectId);
        byte[] pdfBytes = pdfExportService.genererRapportPdf(report);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=rapport-projet-" + projectId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}